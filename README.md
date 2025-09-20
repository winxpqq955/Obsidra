# Obsidra
A simple but efficient tool to register Velocity servers when Paper servers start, coordinated via NATS.

Docker templates and example setups might be provided in the future.

## Modules
- `common`: Stores handlers and common packets for easy manageability between both `paper` and `velocity` modules.
- `paper`: Connects to NATS and sends status packets in order for velocity proxies to know that they have to register a new server.
- `velocity`: Listens for packets through NATS in order to register servers with ease.

## Issue Tracker / Contributions
Is anything not working properly? Feel free to create an issue [here](https://github.com/hardcorefactions/Obsidra/issues).

Do you want to collaborate in the development of this? You can create a pull request with your changes and I will review it.