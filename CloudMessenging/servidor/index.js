const admin = require('firebase-admin');
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');

const serviceAccount = require("./cloud-messenging-41407-firebase-adminsdk-fbsvc-1d79a09a22.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const app = express();
app.use(cors());
app.use(bodyParser.json());
app.use(express.static('public'));

app.post('/send', async (req, res) => {
  const { token, title, body } = req.body;

  if (!token || !title || !body) {
    return res.status(400).send({ error: 'Faltan campos' });
  }

  const message = {
    notification: { title, body },
    data: {
        title: title,
        message: body
    },
    android: {
      priority: "high",
      notification: {
        channelId: "fcm_default_channel"
      }
    },
    token: token
  };

  try {
    const response = await admin.messaging().send(message);
    console.log('Enviado:', response);
    res.status(200).send({ success: true });
  } catch (error) {
    console.error('Error:', error);
    res.status(500).send({ error: error.message });
  }
});

app.listen(3000, () => {
  console.log(`Servidor corriendo en http://localhost:3000`);
});
