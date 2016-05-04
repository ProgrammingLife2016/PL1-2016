import re, sys

class Segment():
    MAX = 80

    def __init__(self, x):
        x = map(self.f, x)
        _, self.id, self.data, _, self.ori, self.crd, self.crdctg, self.ctg, self.start, _ = x
        self.ori = self.ori.split(";")
        self.ctg = self.ctg.split(";")

    def c(self, s):
        return s[:self.MAX - 3] + "..." if len(s) > self.MAX else s

    def f(self, s):
        return s.split(":")[2] if ":" in s else s

    def __str__(self):
        return "\n\t".join([
          "\tID     = {}".format(self.id), "DATA   = {}".format(self.data),
            "ORI    = {}".format(self.ori), "CRD    = {}".format(self.crd),
            "CRDCTG = {}".format(self.crdctg), "CTG    = {}".format(self.ctg),
            "START  = {}".format(self.start)])



file = "TB10.gfa" if len(sys.argv) == 1 else sys.argv[1] 
f = open(file, "r").readlines()

genomes = re.search("ORI:Z:(.*?)$", f[1]).group(1).split(";")
print "\n".join(genomes)
#Create tile view
m = {}
for ref in genomes:
    m[ref] = [" "] * 100

data = map(lambda line: re.split("\s", line), f)
data = filter(lambda x: x[0] == "S", data)
segments = map(lambda x: Segment(x), data)

for seg in segments:
    print "-----------"
    print seg.id 
    print seg
