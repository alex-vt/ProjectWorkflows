#! /usr/bin/octave -qf
# call format:
# ./Adder.m -a file_a_path -b file_b_path -Sum file_sum_path
arg_list = argv ();

# reading a
fid = fopen(arg_list{2}, 'r');
a = fscanf(fid, '%d');
fclose(fid);

# reading b
fid = fopen(arg_list{4}, 'r');
b = fscanf(fid, '%d');
fclose(fid);

# writing Sum
fid = fopen(arg_list{6}, 'w');
a = fprintf(fid, '%d', a + b);
fclose(fid);

# reading Sum
fid = fopen(arg_list{6}, 'r');
fclose(fid);