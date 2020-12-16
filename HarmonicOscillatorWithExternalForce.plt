set terminal pdfcairo enhanced size 29cm,20cm font "Times-New-Roman" fontscale 1.2
set output "HarmonicOscillatorWithExternalForce.pdf"
#set xrange [0:00]
#set yrange [-1.2:1.2]
set label 1 at graph 0.6,0.95 "{/Symbol g}={/Symbol w}+0.1 {/:Italic f}=0.1"
plot "HarmonicOscillatorWithExternalForce-output.txt" notitle
