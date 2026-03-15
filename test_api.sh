#!/bin/sh
TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTc3MjQ1ODU2MiwiZXhwIjoxNzcyNTQ0OTYyfQ.Fkaz7EK1xETdoB6bMN5-WH744b58e8nvEa4230cG6u2gLdTatx9w4fozeKDzLlrK"
curl -s http://localhost:8080/api/products -H "Authorization: Bearer $TOKEN"
