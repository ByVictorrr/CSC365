




def options():
	_input = input("Enter command\n")
	split = _input.split()
	# 7 diff options
	switch(_input[0]):
		case 'S': # student command
			# error check for Students
			if (split[0] != "Student:" or split[0] != "S:"):
				return (None,None)
			else:
				return (_input[0], split[1:])
			break
		case 'T':
			if (split[0] != "Teacher:" or split[0] != "T:"):
				return (None,None)
			else:
				return (_input[0], split[1:])
			break
		case 'G':
			if (split[0] != "Grade:" or split[0] != "G:"):
				return (None,None)
			else:
				return (_input[0], split[1:])
			break
		case 'B':
			if (split[0] != "Bus:" or split[0] != "B:"):
				return (None,None)
			else:
				return (_input[0], split[1:])	
			break
		case 'A':
			if (split[0] != "Average:" or split[0] != "A:"):
				return (None,None)
			else:
				return (_input[0], split[1:])	
			break
		case 'I':
			if (split[0] != "Info" or split[0] != "I"):
				return (None,None);
			else:
				return (_input[0],None)
			break
		case 'Q':
			if (split[0] != "Quit" or split[0] != "Q"):
				return (None,None)
			else:
				return (_input[0],None)
			break
		default:
			break
	return (None,None)

if __name__ == "__main__":

	operation, sub_options = (None,None)
	with open("students.txt") as f:
		_input = input("Enter command\n")
		 split = _input.split()
		# 7 diff options
		switch(_input[0]):
			case 'S': # student command
		# error check for Students
				if (split[0] == "Student:" or split[0] == "S:"):
					operations, sub_options = (_input[0], split[1:])
				break
			case 'T':
				if (split[0] == "Teacher:" or split[0] == "T:"):
					operations, sub_options = (_input[0], split[1:])
				break
			case 'G':
				if (split[0] == "Grade:" or split[0] == "G:"):
					operations, sub_options = (_input[0], split[1:])
				break
			case 'B':
				if (split[0] == "Bus:" or split[0] == "B:"):
					operations, sub_options = (_input[0], split[1:])
				break
			case 'A':
				if (split[0] == "Average:" or split[0] == "A:"):
					operations, sub_options = (_input[0], split[1:])
				break
			case 'I':
				if (split[0] == "Info" or split[0] == "I"):
					operations, sub_options = (_input[0], None)
				break
			case 'Q':
				if (split[0] == "Quit" or split[0] == "Q"):
					operations,sub_options =  (_input[0],None)
				break
			default:
				# output no options provided
				break








