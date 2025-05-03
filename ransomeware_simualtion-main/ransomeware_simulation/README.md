# Ransomware Simulation, Detection, and Mitigation

This project simulates a ransomware attack in a virtual Ubuntu environment and implements real-time monitoring, detection, and automated mitigation.

## 📁 Project Structure
- **encryption/**: AES-256 Java encryption module
- **infection/**: Bash script simulating social engineering
- **monitoring/**: Java WatchService file tracker
- **detection/**: Python script to detect ransomware behavior
- **mitigation/**: Shell script to kill malware and restore backups
- **backup/**: Sample backup files

## ⚙️ Environment Setup

- **OS**: Ubuntu (20.04+)
- **Java**: JDK 8+
- **Python**: 3.8+
- **Required libraries**:
  - Java: WatchService (built-in)
  - Python: `pandas`, `datetime`

## 🔁 How to Run
1. Clone the repository
2. Run `install-driver.sh` to simulate infection
3. Start `FileMonitorLogger.java`
4. Run `detect_ransomware.py` to monitor logs
5. Auto-trigger `mitigation_script.sh` on detection

## 🔗 Third-party Resources
- [Java WatchService Docs](https://docs.oracle.com/javase/tutorial/essential/io/notification.html)
- [Python pandas](https://pandas.pydata.org/)

