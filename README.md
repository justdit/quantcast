NOTE: There's an assumption in task that user of CLI is located within GMT timezone as
per 2nd output it shows:

- SAZuXPGUrfbcn5UA
- 4sMM2LxV07bPJzwf
- fbcn5UAVanZf6UtG

which is not the case if the user located say in Europe/Amsterdam timezone.
Nevertheless, as a developer I default to users timezone because that's the default
assumption user of the tool will have. This breaks the 2nd test case, but behaviour can be changed
as noted in the code.

