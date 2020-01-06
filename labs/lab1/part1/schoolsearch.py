




def options():
	_input = input("Enter command\n")
	split = _input.split()
	# 7 diff options
	switch(_input[0]):
		case 'S': # student command
			# error check for Students
			if (split[0] != "Student:" or split[0] != "S:"):
				return -1
			else:
				return (_input[0], split[1:])
			break
		case 'T':
			if (split[0] != "Teacher:" or split[0] != "T:"):
				return -1
			else:
				return (_input[0], split[1:])
			break
		case 'G':
			if (split[0] != "Grade:" or split[0] != "G:"):
				return -1
			else:
				return (_input[0], split[1:])
			break
		case 'B':
			if (split[0] != "Bus:" or split[0] != "B:"):
				return -1;
			else:
				return (_input[0], split[1:])	
			break
		case 'A':
			if (split[0] != "Average:" or split[0] != "A:"):
				return -1;
			else:
				return (_input[0], split[1:])	
			break
		case 'I':
			if (split[0] != "Info" or split[0] != "I"):
				return -1;
			else:
				return (_input[0],)
			break
		case 'Q':
			if (split[0] != "Quit" or split[0] != "Q"):
				return -1;
			else:
				return (_input[0],)
			break
		default:
			break
	return -1




