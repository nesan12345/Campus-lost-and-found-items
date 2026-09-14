#!/usr/bin/env bash
# ==============================================================================
# Campus Lost & Found Management System - Linux / macOS Test Runner
# ==============================================================================

mkdir -p bin reports

javac -d bin src/com/campus/lostfound/model/*.java \
             src/com/campus/lostfound/exception/*.java \
             src/com/campus/lostfound/util/*.java \
             src/com/campus/lostfound/dao/*.java \
             src/com/campus/lostfound/thread/*.java \
             src/com/campus/lostfound/service/*.java \
             src/com/campus/lostfound/Main.java \
             test/com/campus/lostfound/LostFoundValidationTest.java

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

java -cp bin com.campus.lostfound.LostFoundValidationTest
