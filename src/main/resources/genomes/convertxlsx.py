# Samuel Sital
#
# Convert .xlsx file to .csv file
# > python convertxlsx.py <xlsx file>
#
import xlrd, csv, sys

if len(sys.argv) == 1: exit()
print sys.argv[1]
workbook = xlrd.open_workbook(sys.argv[1])
worksheet = workbook.sheet_by_index(0)
csvfile = open("{}.csv".format(sys.argv[1].split(".")[0]) ,"wb")
wr = csv.writer(csvfile)
for row in range(worksheet.nrows):
    wr.writerow(worksheet.row_values(row))
csvfile.close()
