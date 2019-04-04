set terminal pdfcairo enhanced color size 29cm,20cm font "Times-New-Roman" fontscale 1.2
set output "CoupledOscillators2.pdf"
set xrange [300:100]
#set yrange [-1.2:1.2]
 plot "CoupledOscillators2-output.txt" with line notitle,\
"CoupledOscillators2-output.txt" u 1:3 with line notitle,\
"CoupledOscillators2-output.txt" u 1:4 with line notitle
