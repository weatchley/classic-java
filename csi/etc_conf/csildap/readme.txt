csiladp synchronizer

required files:
./csi.jar
./ojdbc14.jar
./bin/csildaprw
./bin/csildapyd

prerequisites:
- take a look at included .bashrc file and set java related environment variables accordingly
- change the file path in ./bin/csildaprw ./bin/csildapyd to the ones on the server you are installing these components to.

testing instructions:
run ./bin/csildapyd ./bin/csildaprw to trigger the java task.
first try csildapyd it connects to ydservices active directory and delete/add users in csi database. csildaprw runs the same task against rw.doe.gov

crontab configuration:

# Crontab for Systems Monitor.
#
#     minute (0-59),
#     hour (0-23),
#     day of the month (1-31),
#     month of the year (1-12),
#     day of the week (0-6 with 0=Sunday).
#
###every monday at 6AM
0 6 * * 1  /usr/local/homes/higashis/bin/csildapsync > csildapsynch.log 2>&1; mailx -s "CSI LDAP synchronization" shuhei.higashi\@ymp.gov < csildapsync.log







