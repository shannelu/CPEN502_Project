import matplotlib.pyplot as plt
import numpy as np
import re
import os

def extract_data_from_log(file_path):
    with open(file_path, 'r') as file:
        log_text = file.read()

    # Regular expression to extract the epoch range and win rate
    pattern = r"(\d+) - (\d+)  win rate, (\d+)"

    # Extracting data using regular expression
    extracted_data = re.findall(pattern, log_text)

    # Parsing the data into separate lists for ranges and win rates
    epoch_ranges = [(int(start), int(end)) for start, end, rate in extracted_data]
    win_rates = [int(rate) for start, end, rate in extracted_data]

    # Using the midpoint of each epoch range for the x-axis
    epoch_midpoints = [(start + end) / 2 for start, end in epoch_ranges]

    return epoch_midpoints, win_rates

# File paths for the log files
file_paths = [
    '../../out/production/part3/main/java/NNRobot.data/winrate_momentum0.9_gamma0.3.log',
    '../../out/production/part3/main/java/NNRobot.data/winrate_momentum0.9.log',
    '../../out/production/part3/main/java/NNRobot.data/winrate_momentum0.9_gamma0.7.log',
    # '../../out/production/part3/main/java/NNRobot.data/winrate_epsilon0.1.log',
    # '../../out/production/part3/main/java/NNRobot.data/winrate_epsilon0.2.log'
]

plt.figure(figsize=(10, 6))

# for file_path in file_paths:
#     epoch_midpoints, win_rates = extract_data_from_log(file_path)
#     plt.plot(epoch_midpoints, win_rates, label=file_path)

for file_path in file_paths:
    # Extract file name from the file path
    file_name = os.path.basename(file_path)
    print(file_name)
    epoch_midpoints, win_rates = extract_data_from_log(file_path)
    plt.plot(epoch_midpoints, win_rates, label=file_name)

plt.title('Win Rate vs Epoch with different gamma')
plt.xlabel('Epoch')
plt.ylabel('Win Rate (%)')
# plt.grid(True)
plt.legend()
plt.show()

########################################################################################################################

# import matplotlib.pyplot as plt
# import numpy as np
# import re

# winRate1 = []
# # with open ("./robotRunnerLUT_offpolicy.txt", 'r') as file:
# with open('../../out/production/part3/main/java/NNRobot.data/winrate_epsilon0.2.log', 'r') as file:
#     log_text = file.read()


# # Regular expression to extract the epoch range and win rate
# pattern = r"(\d+) - (\d+)  win rate, (\d+)"

# # Extracting data using regular expression
# extracted_data = re.findall(pattern, log_text)

# # Parsing the data into separate lists for ranges and win rates
# epoch_ranges = [(int(start), int(end)) for start, end, rate in extracted_data]
# win_rates = [int(rate) for start, end, rate in extracted_data]

# # Preparing data for plotting
# # Using the midpoint of each epoch range for the x-axis
# epoch_midpoints = [(start + end) / 2 for start, end in epoch_ranges]


# # with open('../../out/production/part3/main/java/NNRobot.data/winrate_epsilon0.2.log', 'r') as file:
# #     log_text = file.read()


# # Plotting
# plt.figure(figsize=(10, 6))
# plt.plot(epoch_midpoints, win_rates)
# plt.title('Win Rate vs Epoch')
# plt.xlabel('Epoch')
# plt.ylabel('Win Rate (%)')
# plt.grid(True)
# plt.show()