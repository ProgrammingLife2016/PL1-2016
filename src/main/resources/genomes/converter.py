#Example
#python gfaconverter.py <data>.gfa <startid> <endid>
#python gfaconverter.py TB10.gfa 2000 3000
import re, sys, os

OUTPUT = "output"
FORMATS = [("pdf","pdf"), ("plain", "txt")]
if len(sys.argv) != 4: exit()
filename = sys.argv[1]
s = int(sys.argv[2])
e = int(sys.argv[3])
f = open(filename, "r").readlines()
lines = map(lambda line: re.split("\\s+", line), f)

dot = filter(lambda line: line[0] == "L", lines)
dot = map(lambda x: (int(x[1]), int(x[3])), dot)
dot = filter(lambda (l, r): s <= l <= e and s <= r <= e, dot)
dot = map(lambda (l, r): (l-s+1, r-s+1), dot)
dot = map(lambda (l, r): "{} -> {};".format(l, r), dot)

def filter_line(data):
    if (data[0] == "S"):
        return s <= int(data[1]) <= e
    elif (data[0] == "L"):
        l, r = int(data[1]), int(data[3])
        return s <= l <= e and s <= r <= e
    else:
        return False

def map_line(data):
    if (data[0] == "S"):
        data[1] = int(data[1])-s+1
    elif (data[0] == "L"):
        data[1] = int(data[1])-s+1
        data[3] = int(data[3])-s+1
    return "\t".join(map(str, data)) + "\n"

#write .gfa file
gfa = f[:2] + map(map_line, filter(filter_line, lines[2:]))
w = open(OUTPUT + ".gfa", "w")
for line in gfa: w.write(line)

#write .dot file
w = open(OUTPUT + ".dot", "w")
w.write("digraph G {\n\trankdir=LR;\n\tnodesep=1.2;\n\tnode[shape=circle];\n")
for line in dot: w.write("\t" + line + "\n")
w.write("}")
w.close()

#generate output from .dot file
for fmt, ext in FORMATS:
    os.system("dot -T{} {} -o {}".format(fmt, OUTPUT + ".dot", OUTPUT + ".{}".format(ext)))