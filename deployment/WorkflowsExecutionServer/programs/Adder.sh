# Parameters
#echo a arg: $1
#echo a filename: $2
#echo b arg: $3
#echo b filename: $4
#echo Sum arg: $5
#echo Sum filename: $6

# Count
a=$(<$2)
b=$(<$4)
c=$(($a+$b))

# Output result to file
echo $c > $6