// import fs from 'fs'
import net from 'net'
import vorpal from 'vorpal'
import { hash, compare } from './hashing'

const cli = vorpal()

cli
  .delimiter('ftd-file-server~$')

cli
  .command('register <username> <password>')
  .description('Registers user on server')
  .action(function (args, callback) {
    let server = net.createConnection({port: 667}, () => {
      let command = 'register'
      let hashed = hash(args.password)
      server.write(JSON.stringify(`ClientMessage: {${command}, content: ${args.username} ${hashed}}\n`))
      this.log('Wrote to server')
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
    })
  })

cli
  .mode('login <username> <password>')
  .description('Logs into server with given Username and Password')
  .init(function (args, callback) {
    let server = net.createConnection({port: 667}, () => {
    })

    cli
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
      .action()

    server.on('data', (data) => {
      this.log(data.toString())
    })

    server.on('end', () => {
      this.log('disconnected from server :(')
      cli.exec('exit')
    })
  })
  .action(function (command, callback) {
    callback()
  })

export default cli
