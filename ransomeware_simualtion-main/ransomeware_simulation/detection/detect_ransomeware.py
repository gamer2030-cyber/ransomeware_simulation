import csv
import os
from datetime import datetime, timedelta

# Detection thresholds
ENC_THRESHOLD = 1
MOD_THRESHOLD = 3
TIME_WINDOW = timedelta(seconds=5)

# Files and paths
log_file = "file_monitor_log.csv"
critical_dir = "/home/ubuntu/Documents/critical"
mitigation_log_file = "mitigation_log.txt"

# Lists to collect events
modification_times = []
enc_creation_events = []

# 🔎 Analyze Monitoring Log
print("🔎 Analyzing file monitor log...")

try:
    with open(log_file, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            timestamp = datetime.fromisoformat(row["Timestamp"])
            file_name = row["File Name"]
            event_type = row["Event Type"]

            # Rule R2: Detect unauthorized .enc file creation
            if file_name.endswith(".enc") and event_type == "ENTRY_CREATE":
                enc_creation_events.append((timestamp, file_name))
                print(f"🚨 Ransomware Detected: Unauthorized encrypted file - {file_name}")

            # Rule R3: Collect modification events
            if event_type == "ENTRY_MODIFY":
                modification_times.append(timestamp)
except FileNotFoundError:
    print("❌ ERROR: Monitoring log file not found.")
    exit(1)

# 🔎 Analyze for rapid modifications (Rule R3)
modification_times.sort()
violation_found = False
for i in range(len(modification_times) - MOD_THRESHOLD + 1):
    if (modification_times[i + MOD_THRESHOLD - 1] - modification_times[i]) <= TIME_WINDOW:
        print(f"⚠️ Policy Violation: Rapid file modifications between {modification_times[i]} and {modification_times[i + MOD_THRESHOLD - 1]}")
        violation_found = True
        break

# 🛡️ Mitigation Function
def run_mitigation():
    print("\n🚨 Initiating Mitigation Actions...\n")

    # (1) Log mitigation activity
    with open(mitigation_log_file, "a") as log:
        log.write("Ransomware detected and mitigation triggered at: " + str(datetime.now()) + "\n")

    # (2) Try to terminate ransomware process
    try:
        os.system("pkill -f 'install-driver.jar'")
        print("🛑 Ransomware process terminated (if running).")
    except Exception as e:
        print("⚠️ Failed to terminate ransomware process:", e)

    # (3) Protect critical directory
    try:
        os.system(f"chmod -w {critical_dir}")
        print(f"🛡️ Write protection enabled on {critical_dir}.")
    except Exception as e:
        print("⚠️ Failed to modify directory permissions:", e)

# 🚀 Trigger Mitigation If Needed
if enc_creation_events or violation_found:
    run_mitigation()
else:
    print("✅ No ransomware behavior detected.")
    print("✅ System is Safe: No mitigation needed.")

