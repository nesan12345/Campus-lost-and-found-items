#!/usr/bin/env bash
# ==============================================================================
# Campus Lost & Found Management System - Linux / macOS Runner
# ==============================================================================

mkdir -p bin reports

echo "[1/2] Compiling Java Source Files..."
javac -d bin src/com/campus/lostfound/model/*.java \
             src/com/campus/lostfound/exception/*.java \
             src/com/campus/lostfound/util/*.java \
             src/com/campus/lostfound/dao/*.java \
             src/com/campus/lostfound/thread/*.java \
             src/com/campus/lostfound/service/*.java \
             src/com/campus/lostfound/Main.java

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "[2/2] Launching System..."
java -cp bin com.campus.lostfound.Main "$@"
