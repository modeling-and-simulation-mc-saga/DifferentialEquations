set terminal pdfcairo enhanced color size 29cm,20cm font "Times-New-Roman" fontscale 1.2
set output "Kuramoto-5.0.pdf"
#set xrange [0:100]
#set yrange [-1.2:1.2]
plot "Kuramoto-5.0-output.txt" u 1:2 with line notitle,\
"Kuramoto-5.0-output.txt" u 1:3 with line notitle,\
"Kuramoto-5.0-output.txt" u 1:4 with line notitle
set output "Kuramoto-0.0.pdf"
plot "Kuramoto-0.0-output.txt" u 1:2 with line notitle,\
"Kuramoto-0.0-output.txt" u 1:3 with line notitle,\
"Kuramoto-0.0-output.txt" u 1:4 with line notitle
