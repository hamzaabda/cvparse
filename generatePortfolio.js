// generatedpdf.js
const PDFDocument = require('pdfkit');
const fs = require('fs');
const path = require('path');

function generatePortfolio(data) {
    // Vérifiez que data.projects est bien un tableau
    if (!Array.isArray(data.projects)) {
        console.error('Expected data.projects to be an array but got:', data.projects);
        data.projects = []; // Valeur par défaut pour éviter l'erreur
    }
    
    // Créer un nouveau document PDF
    const doc = new PDFDocument();
    
    // Définir le chemin du fichier PDF généré
    const filePath = path.join(__dirname, 'portfolio.pdf');
    
    // Créer un flux de sortie pour le fichier PDF
    const writeStream = fs.createWriteStream(filePath);
    doc.pipe(writeStream);

    // Ajouter le contenu au PDF
    doc.fontSize(20).text(`${data.name}'s Portfolio`, { underline: true });
    
    doc.fontSize(16).text('Contact Information', { continued: true }).fontSize(14);
    doc.text(`Email: ${data.email}`);
    doc.text(`Phone: ${data.phone}`);

    doc.fontSize(16).text('Skills', { continued: true }).fontSize(14);
    data.skills.forEach(skill => doc.text(`- ${skill}`));

    doc.fontSize(16).text('Projects', { continued: true }).fontSize(14);
    data.projects.forEach(project => doc.text(`- ${project}`));

    doc.fontSize(16).text('Recommendations', { continued: true }).fontSize(14);
    data.recommendations.forEach(rec => doc.text(`- ${rec}`));

    // Finaliser le document PDF
    doc.end();

    // Retourner le chemin du fichier généré
    return filePath;
}

module.exports = { generatePortfolio };
