import net from 'net'
import vorpal from 'vorpal'
import { hash, compare } from './hashing'

const cli = vorpal()

let server

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
        this.log(`${serverResponse.value}`)
        server.end()
        callback()
      })
    })
  })

cli
  .mode('login <username> <password>')
  .description('Logs into server with given Username and Password')
  .init(function (args, callback) {
      server = net.createConnection({port: 667}, () => {
        let command = 'login'
        server.write(JSON.stringify({clientMessage: {command: command, content: args.username}}) + '\n')
        let a = compare(args.password, /* server returned hash */)

        server.on('data', (data) => {
          const { serverResponse } = JSON.parse(data.toString())
          this.log(`${serverResponse.value}`)
          server.end()
          callback()
        })
      })
    })

    /* cli
      .command('files')
      .description('Retrieves a list of files stored on the server')
      .action()

    cli
      .command('upload <local file path> [path stored in database]')
      .description('Uploads a new file to the server')
      .action()

    cli
      .command('download <database file id> [local file path]')
      .descrition('Retrieves the specified file from the server')
      .action() */

    server.on('data', (data) => {
      const { serverResponse } = JSON.parse(data.toString())
      if (serverResponse.error) {
        this.log(`${serverResponse.message}`)
        callback()
      } else {
        this.log(`${serverResponse.data}`)
        callback()
      }
    })

    server.end()
  })
  .action(function (command, callback) {
    callback()
  })

export default cli
