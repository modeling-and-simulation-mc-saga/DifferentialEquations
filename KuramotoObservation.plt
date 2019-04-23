set terminal pdfcairo enhanced color size 29cm,20cm font "Times-New-Roman" fontscale 1.2
set output "Observation-5.0.pdf"
#set xrange [0:100]
#set yrange [-1.2:1.2]
plot "Observation-5.0-output.txt" u 1:2 with line notitle
