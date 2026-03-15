#!/bin/sh
curl -s -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"testuser","password":"123456","realName":"测试用户"}'
