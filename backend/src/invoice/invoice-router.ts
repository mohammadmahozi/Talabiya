import { Router } from 'express'
import type { Invoice } from './invoice'
import { generateInvoicePdf } from './invoice-pdf-generator'
import { sendInvoiceEmail } from './email-service'

const router = Router()

router.post('/send', async (req, res) => {
  const invoices: Invoice[] = req.body

  for (const invoice of invoices) {
    const pdf = await generateInvoicePdf(invoice)
    await sendInvoiceEmail(invoice, pdf)
  }
  res.json({ status: 'sent' })
})

export default router