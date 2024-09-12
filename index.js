const express = require('express');
const bodyParser = require('body-parser');
const fileUpload = require('express-fileupload');
const path = require('path');
const pdfParse = require('pdf-parse');
const { parseDocx } = require('./docxParser'); // Assurez-vous d'implémenter ce module
const { generatePortfolio } = require('./generatedpdf'); // Assurez-vous d'implémenter ce module

const app = express();
const port = 3001;

app.use(bodyParser.json());
app.use(fileUpload()); // Pour gérer les fichiers téléchargés
app.use('/downloads', express.static(path.join(__dirname, 'downloads')));

// Endpoint pour générer un portfolio
app.post('/generate-portfolio', (req, res) => {
    const portfolioData = req.body;

    try {
        const filePath = generatePortfolio(portfolioData);

        res.status(200).json({ 
            message: 'Portfolio generated successfully!', 
            downloadUrl: `/downloads/portfolio.html` 
        });
    } catch (error) {
        console.error('Error generating portfolio:', error);
        res.status(500).json({ message: 'Failed to generate portfolio' });
    }
});

// Endpoint pour analyser un CV
app.post('/analyze-cv', async (req, res) => {
    const file = req.files.file;

    try {
        let text;

        if (file.mimetype === 'application/pdf') {
            const data = await pdfParse(file.data);
            text = data.text;
        } else if (file.mimetype === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document') {
            text = await parseDocx(file.data);
        } else {
            return res.status(400).json({ message: 'Unsupported file type' });
        }

        // Implémentez ici votre logique d'analyse de texte
        const analysisResults = analyzeText(text); // Assurez-vous d'implémenter analyzeText

        res.status(200).json({ analysis: analysisResults });
    } catch (error) {
        console.error('Error analyzing CV:', error);
        res.status(500).json({ message: 'Failed to analyze CV' });
    }
});

// Endpoint pour obtenir des modèles de CV
app.get('/cv-templates', (req, res) => {
    const templates = [
        { id: 1, name: 'Template 1', url: '/templates/template1.html' },
        { id: 2, name: 'Template 2', url: '/templates/template2.html' }
    ];
    res.status(200).json({ templates });
});

app.listen(port, () => {
    console.log(`Node.js service running on port ${port}`);
});
