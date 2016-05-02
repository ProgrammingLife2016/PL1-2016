# Samuel Sital
#
# Convert .xlsx file to .csv file
# > python convertxlsx.py <xlsx file>
#
import xlrd, csv, sys

if len(sys.argv) == 1: exit()
workbook = xlrd.open_workbook(sys.argv[1])
worksheet = workbook.sheet_by_index(0)
csvfile = open("metadata.csv" ,"wb")
wr = csv.writer(csvfile, quoting=csv.QUOTE_ALL)
for row in range(worksheet.nrows):
    wr.writerow(worksheet.row_values(row))
csvfile.close()
