#ifndef XSTREAM_LOGIN_LOGIN_H
#define XSTREAM_LOGIN_LOGIN_H


#include <iostream>
#include "../data/user/account.h"
#include <fstream>


int login(session *use);

int openFile(std::string const fileName);

int readFile(std::ifstream *userAccounts);

Account loadData(std::string record);

int checkUser(std::string name, session *use);


#endif // XSTREAM_LOGIN_LOGIN_H

