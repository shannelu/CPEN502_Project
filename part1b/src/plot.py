import matplotlib.pyplot as plt


# path = "/Users/shannylu/Desktop/CPEN502/CPEN502_Project/part1b/out/production/part1b/BumbleBee.data/"

# path_save = "/Users/shannylu/Desktop/CPEN502/CPEN502_Project/part1b/src/"

# logname1_3a="epsilon=0.log"
# logname2_3a="epsilon=01.log"
# logname3_3a="epsilon=02.log"
# logname4_3a="epsilon=05.log"
# logname5_3a="epsilon=08.log"

# label1_3a="epsilon = 0.0"
# label2_3a="epsilon = 0.1"
# label3_3a="epsilon = 0.2"
# label4_3a="epsilon = 0.5"
# label5_3a="epsilon = 0.8"



# round1_3a = []
# winningrate1_3a = []
# f1_3a = open(path+"/"+logname1_3a)
# line1_3a = f1_3a.readline()
# round2_3a = []
# winningrate2_3a = []
# f2_3a = open(path+"/"+logname2_3a)
# line2_3a = f2_3a.readline()
# round3_3a = []
# winningrate3_3a = []
# f3_3a = open(path+"/"+logname3_3a)
# line3_3a = f3_3a.readline()
# round4_3a = []
# winningrate4_3a = []
# f4_3a = open(path+"/"+logname4_3a)
# line4_3a = f4_3a.readline()
# round5_3a = []
# winningrate5_3a = []
# f5 = open(path+"/"+logname5_3a)
# line5 = f5.readline()
# while line1_3a:
#     round1_3a.append(float(line1_3a.split(' ')[0]))
#     winningrate1_3a.append(float(line1_3a.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line1_3a = f1_3a.readline()
# f1_3a.close()
# while line2_3a:
#     round2_3a.append(float(line2_3a.split(' ')[0]))
#     winningrate2_3a.append(float(line2_3a.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line2_3a = f2_3a.readline()
# f2_3a.close()
# while line3_3a:
#     round3_3a.append(float(line3_3a.split(' ')[0]))
#     winningrate3_3a.append(float(line3_3a.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line3_3a = f3_3a.readline()
# f3_3a.close()
# while line4_3a:
#     round4_3a.append(float(line4_3a.split(' ')[0]))
#     winningrate4_3a.append(float(line4_3a.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line4_3a = f4_3a.readline()
# f4_3a.close()
# while line5:
#     round5_3a.append(float(line5.split(' ')[0]))
#     winningrate5_3a.append(float(line5.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line5 = f5.readline()
# f5.close()

# graph = plt.plot(round1_3a, winningrate1_3a, round2_3a, winningrate2_3a, round3_3a, winningrate3_3a, round4_3a, winningrate4_3a, round5_3a, winningrate5_3a)
# plt.setp(graph[0], color='blue', label=label1_3a)
# plt.setp(graph[1], color='green', label=label2_3a)
# plt.setp(graph[2], color='red', label=label3_3a)
# plt.setp(graph[3], color='purple', label=label4_3a)
# plt.setp(graph[4], color='gray', label=label5_3a)
# plt.legend(loc=4)
# plt.xlabel('number of 100 rounds')
# plt.ylabel('Winning rate')
# plt.title("Comparison of training performance with different epsilon")
# plt.savefig(path_save+"3a"+".png")
# plt.show()


##########################################################################################################################################

# logname1_c = "Intermediate_offpolicy_epsilon=0.log"
# logname2_c = "Terminal_offpolicy_epsilon=0.log"

# label1_c = "Intermediate Rewards"
# label2_c = "Terminal Rewards"

# round1_c = []
# winningrate1_c = []
# f1_c = open(path + "/" + logname1_c)
# line1_c = f1_c.readline()
# round2_c = []
# winningrate2_c = []
# f2_c = open(path + "/" + logname2_c)
# line2_c = f2_c.readline()

# while line1_c:
#     round1_c.append(float(line1_c.split(' ')[0]))
#     winningrate1_c.append(float(line1_c.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line1_c = f1_c.readline()
# f1_c.close()

# while line2_c:
#     round2_c.append(float(line2_c.split(' ')[0]))
#     winningrate2_c.append(float(line2_c.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line2_c = f2_c.readline()
# f2_c.close()

# graph_c1 = plt.plot(round1_c, winningrate1_c, round2_c, winningrate2_c)
# plt.setp(graph_c1[0], color='green', label=label1_c)
# plt.setp(graph_c1[1], color='blue', label=label2_c)

# plt.legend(loc=4)
# plt.xlabel('number of 100 rounds')
# plt.ylabel('Winning rate')
# plt.title("terminal rewards VS intermediate rewards")
# plt.savefig(path_save + "2c-offpolicy" + ".png")
# plt.show()

##########################################################################################################################################

# logname1_a = "BumbleBee_onpolicy_epsilon=0.2.log"
# logname2_a = "BumbleBee_offpolicy_epsilon=0.2.log"

# label1_a = "onPolicy"
# label2_a = "offPolicy"

# round1_a = []
# winningrate1_a = []
# f1_a = open(path+"/"+logname1_a)
# line1_a = f1_a.readline()
# round2_a = []
# winningrate2_a = []
# f2_a = open(path+"/"+logname2_a)
# line2_a = f2_a.readline()

# while line1_a:
#     round1_a.append(float(line1_a.split(' ')[0]))
#     winningrate1_a.append(float(line1_a.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line1_a = f1_a.readline()
# f1_a.close()
# while line2_a:
#     round2_a.append(float(line2_a.split(' ')[0]))
#     winningrate2_a.append(float(line2_a.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line2_a = f2_a.readline()
# f2_a.close()

# graph1 = plt.plot(round1_a, winningrate1_a, round2_a, winningrate2_a)
# plt.setp(graph1[0], color='green', label=label1_a)
# plt.setp(graph1[1], color='blue', label=label2_a)
# plt.legend(loc=4)
# plt.xlabel('number of 100 rounds')
# plt.ylabel('Winning rate')
# plt.title("onPolicy VS offPolicy learning performance with epsilon = 0.2")
# plt.savefig(path_save+"2bepsilon=0.2.png")
# plt.show()

# graph2 = plt.plot(round3_a, winningrate3_a, round4_a, winningrate4_a)
# plt.setp(graph2[0], color='orange', label=label3_a)
# plt.setp(graph2[1], color='purple', label=label4_a)
# plt.legend(loc=4)
# plt.xlabel('# of 100 rounds')
# plt.ylabel('Winning rate')
# plt.title("2-b)- on-policy learning VS off-policy performance(terminal rewards)")
# plt.savefig(path_save+"result/2-b-terminal.png")
# plt.show()



##########################################################################################################################################


filename = "/Users/shannylu/Desktop/CPEN502/CPEN502_Project/part1b/out/production/part1b/BumbleBee.data/benchmark.log"
round = []
winningrate = []
f = open(filename)
line = f.readline()
while line:
    round.append(float(line.split(' ')[0]))
    winningrate.append(float(line.split(' ')[1].replace('\n', '').replace('\r', '')))
    line = f.readline()
f.close()
# print(round)
# print(winningrate)
plt.plot(round, winningrate)
plt.xlabel('number of 100 rounds')
plt.ylabel('Winning rate')
plt.title('Benchmark: win rate over numbers of round with learning rate = 0')

plt.savefig("/Users/shannylu/Desktop/CPEN502/CPEN502_Project/part1b/src/benchmark.png")
plt.show()


# logname = filename[0]
# round = []
# winningrate = []
# f = open(path+"/"+logname)
# line = f.readline()
# while line:
#     round.append(float(line.split(' ')[0]))
#     winningrate.append(float(line.split(' ')[1].replace('\n', '').replace('\r', '')))
#     line = f.readline()
# f.close()
# plt.plot(round, winningrate)
# plt.xlabel('# of 100 rounds')
# plt.ylabel('Winning rate')
# plt.title("2-a)-progress of learning with winning rate")
# plt.savefig(path_save+"result/"+"2a"+".png")
# plt.show()

##########################################################################################################################################