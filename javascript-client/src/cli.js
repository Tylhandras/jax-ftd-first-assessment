import fs from 'fs'
import net from 'net'
import vorpal from 'vorpal'
import { hash, compare } from './hashing'

const cli = vorpal()

let server
let Username

cli
  .delimiter('ftd-file-server~$')

cli
  .command('register <username> <password>')
  .description('Registers user on server')
  .action(function (args, callback) {
    server = net.createConnection({port: 667}, () => {
      let command = 'register'
      let a = hash(args.password)
      a.then(function (hashedPassword) {
        server.write(JSON.stringify({clientMessage: {command: command, content: args.username + ' ' + hashedPassword}}) + '\n')
      })

      server.on('data', (data) => {
        const { serverResponse } = JSON.parse(data.toString())
        this.log(`${serverResponse.data.value}`)
        server.end()
        callback()
      })
    })
  })

cli
  .mode('login <username> <password>')
  .description('Logs into server with given Username and Password')
  .delimiter('Connected:')
  .init(function (args, callback) {
    server = net.createConnection({port: 667}, () => {
      Username = args.username
      let command = 'login'
      server.write(JSON.stringify({clientMessage: {command: command, content: args.username}}) + '\n')

      server.on('data', (data) => {
        const { serverResponse } = JSON.parse(data.toString())
        let a = compare(args.password, serverResponse.data.value)
        a.then(function (successFlag) {
          if (successFlag) {
            cli.log('Successfully logged in')
          } else {
            cli.log('Failed to log in')
          }
        })
        server.end()
        callback()
      })
    })
  })
  .action(function (command, callback) {
    callback()
  })

 /* Can be declared anywhere.  Will have to use boolean flag to allow command
 execution inside login only.  Boolean flag to be init to false.  Login will
 set true if login successful.  Flag set to false on logout. */
cli
   .command('files')
   .description('Retrieves a list of files stored on the server')
   .action(function (args, callback) {
     server = net.createConnection({port: 667}, () => {
       let command = 'files'
       server.write(JSON.stringify({clientMessage: {command: command, content: Username}}) + '\n')

       server.on('data', (data) => {
         const { serverResponse } = JSON.parse(data.toString())
         cli.log(serverResponse.data.value)
       })
       server.end()
       callback()
     })
   })

cli
   .command('upload <localPath> [path stored in database]')
   .description('Uploads a new file to the server')
   .action(function (args, callback) {
     server = net.createConnection({port: 667}, () => {
       let command = 'upload'
       let a = fs.readFile(args.localPath)
       let fileData = Buffer(a).toString('base64')
       server.write(JSON.stringify({clientMessage: {command: command, content: Username + ' ' + args.localPath + ' ' + fileData}}) + '\n')

       server.on('data', (data) => {
         const { serverResponse } = JSON.parse(data.toString())
         cli.log(serverResponse.data.value)
       })
       fs.close()
       server.end()
       callback()
     })
   })

cli
   .command('download <databaseId> [local file path]')
   .descrition('Retrieves the specified file from the server')
   .action(function (args, callback) {
     server = net.createConnection({port: 667}, () => {
       let command = 'download'
       server.write(JSON.stringify({clientMessage: {command: command, content: Username + ' ' + args.databaseId}}) + '\n')

       server.on('data', (data) => {
         const { serverResponse } = JSON.parse(data.toString())
         cli.log(serverResponse.data.value)
       })
       server.end()
       callback()
     })
   })

export default cli
