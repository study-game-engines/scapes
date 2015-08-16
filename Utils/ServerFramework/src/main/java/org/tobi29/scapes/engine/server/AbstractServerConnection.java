/*
 * Copyright 2012-2015 Tobi29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tobi29.scapes.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobi29.scapes.engine.utils.SleepUtil;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.engine.utils.task.Joiner;
import org.tobi29.scapes.engine.utils.task.TaskExecutor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractServerConnection<P>
        implements PlayConnection<P> {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(AbstractServerConnection.class);
    protected final TaskExecutor taskExecutor;
    private final int workerCount;
    private final List<NetWorkerThread> workers = new ArrayList<>();
    private final byte[] connectionHeader;
    private Joiner joiner;

    protected AbstractServerConnection(TaskExecutor taskExecutor,
            byte[] connectionHeader, TagStructure tagStructure) {
        this.taskExecutor = taskExecutor;
        this.connectionHeader = connectionHeader;
        workerCount = tagStructure.getInteger("WorkerCount");
    }

    public int start(int port) throws IOException {
        InetSocketAddress address = start(new InetSocketAddress(port));
        return address.getPort();
    }

    @SuppressWarnings("unchecked")
    public <A extends SocketAddress> A start(A address) throws IOException {
        LOGGER.info("Starting worker {} threads...", workerCount);
        List<Joiner> joiners = new ArrayList<>(workerCount + 1);
        for (int i = 0; i < workerCount; i++) {
            NetWorkerThread worker = new NetWorkerThread();
            joiners.add(taskExecutor.runTask(worker, "Connection-Worker-" + i));
            workers.add(worker);
        }
        LOGGER.info("Starting socket thread...");
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(address);
        joiners.add(taskExecutor.runTask(joiner -> {
            try {
                while (!joiner.marked()) {
                    SocketChannel client = channel.accept();
                    if (client == null) {
                        SleepUtil.sleep(100);
                    } else {
                        client.configureBlocking(false);
                        int load = Integer.MAX_VALUE;
                        NetWorkerThread bestWorker = null;
                        for (NetWorkerThread worker : workers) {
                            int workerLoad = worker.connections.size();
                            if (workerLoad < load) {
                                bestWorker = worker;
                                load = workerLoad;
                            }
                        }
                        if (bestWorker == null) {
                            client.close();
                        } else {
                            bestWorker.addConnection(
                                    new UnknownConnection(client, this,
                                            connectionHeader));
                        }
                    }
                }
            } finally {
                channel.close();
            }
        }, "Socket"));
        joiner = new Joiner(joiners);
        return (A) channel.getLocalAddress();
    }

    public void stop() {
        joiner.join();
    }

    protected abstract Optional<Connection> newConnection(SocketChannel channel,
            byte id) throws IOException;

    public static class NetWorkerThread implements TaskExecutor.ASyncTask {
        private final Queue<Connection> connectionQueue =
                new ConcurrentLinkedQueue<>();
        private final List<Connection> connections = new ArrayList<>();
        private final Selector selector;

        public NetWorkerThread() throws IOException {
            selector = Selector.open();
        }

        public void addConnection(Connection connection) throws IOException {
            connectionQueue.add(connection);
            connection.register(selector, SelectionKey.OP_READ);
            selector.wakeup();
        }

        @Override
        public void run(Joiner joiner) {
            try {
                while (!joiner.marked()) {
                    boolean processing = false;
                    while (!connectionQueue.isEmpty()) {
                        Connection connection = connectionQueue.poll();
                        connections.add(connection);
                    }
                    for (Connection connection : connections) {
                        if (connection.tick(this)) {
                            processing = true;
                        }
                    }
                    Iterator<Connection> iterator = connections.iterator();
                    while (iterator.hasNext()) {
                        Connection connection = iterator.next();
                        if (connection.isClosed()) {
                            try {
                                connection.close();
                            } catch (IOException e) {
                                LOGGER.warn("Failed to close connection: {}",
                                        e.toString());
                            }
                            iterator.remove();
                        }
                    }
                    if (!processing && !joiner.marked()) {
                        try {
                            selector.select(10);
                            selector.selectedKeys().clear();
                        } catch (IOException e) {
                            LOGGER.warn("Error when waiting for events: {}",
                                    e.toString());
                        }
                    }
                }
            } finally {
                for (Connection connection : connections) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        LOGGER.warn("Failed to close connection: {}",
                                e.toString());
                    }
                }
                try {
                    selector.close();
                } catch (IOException e) {
                    LOGGER.warn("Failed to close selector: {}", e.toString());
                }
            }
        }
    }
}
