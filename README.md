# HardwareScanner 

A lightweight **Java Swing** desktop application that fetches and displays your Windows PC's full system information from systeminfo[cmd]. Display CPU, RAM, BIOS, OS details, network adapters, hotfixes, and more in a clean, scrollable GUI.

## Features

- **Live System Scan** — Runs `systeminfo` under the hood and parses the output into a readable key/value table.
- **Rescan** — Re-fetch up-to-date hardware and OS info at any time with a single click.
- **Progress Indicator** — An indeterminate progress bar shows while the scan runs in the background (non-blocking UI via `SwingWorker`).
- **Export to File** — Save the last scan results to a `.txt` file via `File → Save`.
- **Input Validation** — Enforces valid filenames before saving to prevent errors.

## Requirements

- Windows OS (relies on the `systeminfo` command)
- Java 8+
- Maven (for building)

## Option 1: Build & Run

IDE Terminal
mvn package
java -jar target/HardwareScanner.jar

## Option 2: Releases

Download the jar file and run the program.
