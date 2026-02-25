import PDFDocument from 'pdfkit'
import type { Invoice } from './invoice'

export function generateInvoicePdf(invoice: Invoice): Promise<Buffer> {
  return new Promise((resolve, reject) => {
    const doc = new PDFDocument()
    const chunks: Buffer[] = []

    doc.on('data', (chunk) => chunks.push(chunk))
    doc.on('end', () => resolve(Buffer.concat(chunks)))
    doc.on('error', reject)

    // Header
    doc.fontSize(18).text(`Invoice for ${invoice.user.name}`, { underline: true })
    doc.moveDown()

    // Period
    doc.fontSize(12).text(`Period: ${invoice.summary.from} to ${invoice.summary.to}`)
    doc.text(`Orders Placed: ${invoice.summary.ordersPlaced}`)
    doc.text(`Orders Covered: ${invoice.summary.ordersCovered}`)
    doc.moveDown()

    // Totals summary
    doc.fontSize(14).text('Summary', { underline: true })
    doc.fontSize(12).text(`Total Paid: ${invoice.totals.totalPaid}`)
    doc.text(`Total Owed: ${invoice.totals.totalOwed}`)
    doc.text(`Final Total: ${invoice.totals.finalTotal} (${invoice.totals.direction})`)
    doc.moveDown()

    // Orders breakdown
    doc.fontSize(14).text('Order Breakdown', { underline: true })
    doc.moveDown(0.5)

    invoice.orders.forEach((order) => {
      doc.fontSize(13).text(order.restaurant, { underline: true })
      doc.fontSize(12).text(`Total Paid: ${order.totalPaid}`)
      doc.text(`Total Owed: ${order.totalOwed}`)
      doc.moveDown(0.5)

      order.items.forEach((item) => {
        doc.text(`  ${item.name} x${item.quantity} - ${item.price} (Total: ${item.total})`)
      })
      doc.moveDown()
    })

    doc.end()
  })
}
