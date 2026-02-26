import nodemailer from 'nodemailer'
import type { Invoice } from './invoice'


const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: process.env.EMAIL,
    pass: process.env.EMAIL_PASSWORD
  }
})

export async function sendInvoiceEmail(
  invoice: Invoice,
  pdf: Buffer,
): Promise<void> {
  await transporter.sendMail({
    from: process.env.EMAIL,
    to: invoice.user.email,
    subject: `Invoice for ${invoice.user.name}`,
    text: `Hi ${invoice.user.name}, please find your invoice attached.`,  
    attachments: [
        {
          filename: `invoice-${invoice.user.name}.pdf`,
          content: pdf
        } 
      ]
    })
}