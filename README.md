The app ist curretly running on a private server.
To release the new version upload the new jar to SG Server/home/bierkasse/app
Then connect via ssh and restart it with:
`sudo systemctl stop bierkasse`
`sudo systemctl start bierkasse`
To check the state use:
`sudo systemctl status bierkasse`
