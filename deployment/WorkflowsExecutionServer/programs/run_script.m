#! /usr/bin/octave -qf
# call format:
# ./run_script.m -script file_script_path -arguments file_arg_path -output file_output_path
arg_list = argv ();

# retrieving script
scriptName = arg_list{2};

# retrieving arguments
fid = fopen(arg_list{4}, 'r');
argumentsList = fgetl(fid);
#argumentsList = cstrcat(argumentsList, " -", fgetl(fid));
fclose(fid);

# runnung script
launchCommand = cstrcat("octave -qf ", scriptName, " ", argumentsList);
[output, status] = system(launchCommand);

# writing script's output
fid = fopen(arg_list{6}, 'w');
fprintf(fid, '%s', output);
fprintf(fid, '%s', status);
fclose(fid);
