const admin = require('firebase-admin');
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const path = require('path');

// 1. Inicialización de Firebase Admin
// Usamos el archivo de credenciales que moviste
const serviceAccount = require("./cloud-messenging-41407-firebase-adminsdk-fbsvc-1d79a09a22.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Servir la interfaz web (carpeta public)
app.use(express.static('public'));

// 2. Endpoint para enviar notificaciones
app.post('/send', async (req, res) => {
  const { token, title, body } = req.body;

  if (!token || !title || !body) {
    return res.status(400).send({ error: 'Faltan campos: token, title o body' });
  }

  const message = {
    notification: {
      title: title,
      body: body
    },
    // Añadimos 'data' para que la app Android pueda capturar el texto
    // y actualizar la interfaz de Compose automáticamente.
    data: {
      message: body
    },
    token: token
  };

  try {
    const response = await admin.messaging().send(message);
    console.log('Mensaje enviado con éxito:', response);
    res.status(200).send({ success: true, messageId: response });
  } catch (error) {
    console.error('Error al enviar mensaje:', error);
    res.status(500).send({ error: error.message });
  }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Servidor FCM corriendo en http://localhost:${PORT}`);
});
