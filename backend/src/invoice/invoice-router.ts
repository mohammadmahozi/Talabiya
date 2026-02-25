import { Router } from 'express'
import type { Invoice } from './invoice'
import { generateInvoicePdf } from './invoice-pdf-generator'

const router = Router()

router.post('/send', async (req, res) => {
  const invoices: Invoice[] = req.body
  const pdfs: Buffer[] = []
  for(const invoice of invoices) {
    const pdf = await generateInvoicePdf(invoice)
    pdfs.push(pdf)
  }
  res.setHeader('Content-Type', 'application/pdf')
  res.send(pdfs[0])
})

export default router