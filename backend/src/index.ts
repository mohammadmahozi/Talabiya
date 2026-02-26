import 'dotenv/config'
import express from 'express'
import invoiceRouter from './invoice/invoice-router'


const app = express()
const port = 3000

app.use(express.json())

app.use('/invoices', invoiceRouter)

app.get('/health', (req, res) => {
  res.json({ status: 'ok'})
})

app.listen(port, () => {
  console.log('Server running on http://localhost:${port}')
})