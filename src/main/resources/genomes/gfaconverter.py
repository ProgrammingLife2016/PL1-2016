import re, sys, os
OUTPUT = "output"
FORMAT = "pdf"
filename = "./PL1-2016/target/classes/genomes/testGraph.gfa" if len(sys.argv) == 1 else sys.argv[1]
f = open(filename, "r").readlines()
data = map(lambda line: re.split("\\s+", line), f)
lines = filter(lambda line: line[0] == "L", data)
gfa_lines = map(lambda x: x[1] + " -> " + x[3] + ";", lines)
w = open(OUTPUT + ".dot", "w")
w.write("digraph G {\n\trankdir=LR;\n\tnodesep=1.2;\n\tnode[shape=circle];\n")
for line in gfa_lines: w.write("\t" + line + "\n")
w.write("}")
w.close()
os.system("dot -T{} {} -o {}".format(FORMAT, OUTPUT + ".dot", OUTPUT + ".{}".format(FORMAT)))
