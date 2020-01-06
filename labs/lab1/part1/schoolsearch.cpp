#include <iostream>
#include <string>
#include <list>
#include <fstream>
#include <sstream>
#include <algorithm>
#include <vector>

struct Student{
	std::string stLastName;
	std::string stFirstName;
	int grade;
	int classRoom;
	int busRoute;
	float gpa;
	std::string tLastName;
	std::string tFirstName;
};

void split_string(std::vector<std::string> &command_pieces, const std::string command){
	std::stringstream ss(command);
	command_pieces.clear();
	for(std::string s; ss >> s;)
		command_pieces.push_back(s);
}



auto iterator_vec(std::list<Student> s, std::string var, std::string operation){
	std::vector<std::list<Student>::iterator> v_it;
	
	auto it = find_if(s.begin(), s.end(), [](Student s){
		s.
	});
	while(it != s.end()){
		v_it.push_back(it);
		it = find_if(++it, s.end(), expr);
	}
	return v_it;
}



int main(){

	std::string command;
	bool isRunning=true; // for quit set to false

	std::list<Student*> students;
	std::fstream file{"students.txt"};
	// step 0 - tablulate the s_list
	Student *n_student;

	std::string line;
	if(file.is_open()){
		while(!file.eof()){
			n_student = new Student;
			std::getline(file, line); // get a line from the file
			std::replace(line.begin(), line.end(), ',', ' ');
			std::istringstream ss(line); // use a ss to orangize data
			ss >> n_student->stLastName >> n_student->stFirstName >> n_student->grade
			   >> n_student->classRoom >> n_student->busRoute >> n_student->gpa
			   >> n_student->tLastName >> n_student->tFirstName; 
			students.push_back(n_student);
		}
	}else{
		std::cerr << "Could not open students.txt" << std::endl;
		return -1;
	}




	// Step 1 - read file into data structure
	
	std::vector<std::string> command_pieces;
	// Step 2 - get options to search the data structure 
	while(isRunning){
		std::cin >> command;
		split_string(command_pieces, command);
		switch(command[0]){
			case 'S':	
				if( command_pieces[0] == "Student:" | command_pieces[0] == "S"){
					if((command_pieces.size() == 3 && (command_pieces.at(2) == "B" | command_pieces.at(2) == "Bus")) | command_pieces.size() == 2 ){
						auto it = find_if(students.begin(), students.end(), [](Student s){s.stLastName == command_pieces[1]});
						while(it != students.end()){
							//print

							it = find_if(++it, students.end(), [](Student s){s.stLastName == command_pieces[1]});
						}
					}
				}
				break;
			case 'T':
				break;
			case 'B':
				break;
			case 'G':
				break;
			case 'A':
				break;
			case 'I':
				break;
			case 'Q':
				if( command_pieces[0] == "Quit" | command_pieces[0] == "Q")
					isRunning=false;
				break;
			default:
				std::cout << "Enter a valid command" << std::endl;
				break;
		}

	}


	return 0;
}
