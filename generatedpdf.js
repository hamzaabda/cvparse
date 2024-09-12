const PDFDocument = require('pdfkit');
const fs = require('fs');
const path = require('path');

function generatePortfolio(data) {
    // Vérifiez que data.projects est bien un tableau
    if (!Array.isArray(data.projects)) {
        console.error('Expected data.projects to be an array but got:', data.projects);
        data.projects = []; // Valeur par défaut si vous voulez éviter l'erreur
    }

    // Vérifiez que data.recommendations est bien un tableau
    if (!Array.isArray(data.recommendations)) {
        console.error('Expected data.recommendations to be an array but got:', data.recommendations);
        data.recommendations = []; // Valeur par défaut si vous voulez éviter l'erreur
    }

    // Exemple de logique pour générer le portfolio en HTML
    const htmlContent = `
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Portfolio</title>
        <style>
            body { font-family: Arial, sans-serif; margin: 20px; }
            h1 { color: #333; }
            .section { margin-bottom: 20px; }
            .section h2 { color: #555; }
            .section p { margin: 5px 0; }
        </style>
    </head>
    <body>
        <h1>${data.name}'s Portfolio</h1>
        <div class="section">
            <h2>Contact Information</h2>
            <p>Email: ${data.email}</p>
            <p>Phone: ${data.phone}</p>
        </div>
        <div class="section">
            <h2>Skills</h2>
            <ul>
                ${data.skills.map(skill => `<li>${skill}</li>`).join('')}
            </ul>
        </div>
        <div class="section">
            <h2>Projects</h2>
            <ul>
                ${data.projects.map(project => `<li>${project}</li>`).join('')}
            </ul>
        </div>
        <div class="section">
            <h2>Recommendations</h2>
            <ul>
                ${data.recommendations.map(rec => `<li>${rec}</li>`).join('')}
            </ul>
        </div>
    </body>
    </html>`;

    // Définir le chemin du fichier HTML généré
    const filePath = path.join(__dirname, 'portfolio.html');
    
    // Écrire le contenu HTML dans un fichier
    fs.writeFileSync(filePath, htmlContent, 'utf8');

    return filePath; // Retourner le chemin du fichier généré
}

module.exports = { generatePortfolio };
