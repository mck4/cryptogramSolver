import sys

#count = 1
with open('litemind-quotes.csv', 'r') as file:
    listy = file.readlines()

    for line in listy[1:]:
        tokens = [] # Empty list

        # Split line into two parts, the # and the rest
        # This is because some quotes have punctuation
        part_one = line.rstrip("\n").split(",", 1)

        # Add it to list
        tokens.append(part_one[0])

        # Split the second half 
        part_two = part_one[1].rsplit(",", 1)

        # Filter out really long quotes
        if len(part_two[0]) > 46:
            continue

        # Add quotes & authors to list
        tokens.append(part_two[0].strip('\"'))
        tokens.append(part_two[1])

        # Debug
        #print(tokens)

        #count += 1

        # Write to file
        with open('new_quotes.txt','a+') as outfile:
            outfile.write("%s|%s\n" % (tokens[1], tokens[2]))

