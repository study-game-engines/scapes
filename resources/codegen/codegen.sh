#!/bin/bash

findHome() {
    local source="${BASH_SOURCE[0]}"
    while [ -h "$source" ] ; do
        local linked="$(readlink "$source")"
        local dir="$(cd -P $(dirname "$source") && cd -P $(dirname "$linked") && pwd)"
        source="$dir/$(basename "$linked")"
    done
    (cd -P "$(dirname "$source")" && pwd)
}

CODEGEN_HOME="$(findHome)"
ENGINE_HOME="$(realpath "$CODEGEN_HOME/../..")"

"$CODEGEN_HOME/GenArrays.kts" > "$ENGINE_HOME/Utils/src/main/kotlin/org/tobi29/scapes/engine/utils/Arrays.kt"
for arrayType in Boolean Byte Short Int Long Float Double Char; do
    "$CODEGEN_HOME/GenArrays.kts" "$arrayType" > "$ENGINE_HOME/Utils/src/main/kotlin/org/tobi29/scapes/engine/utils/${arrayType}Arrays.kt"
done

"$CODEGEN_HOME/GenNumberConversions.kts" > "$ENGINE_HOME/Utils/src/main/kotlin/org/tobi29/scapes/engine/utils/NumberConversions.kt"
