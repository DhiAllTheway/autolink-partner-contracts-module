<?php 
 
 namespace App\Service;

use Twilio\Rest\Client;

class TwilioService
{
    private $twilioClient;

    public function __construct(string $twilioSid, string $twilioAuthToken)
    {
        // Use the passed credentials to initialize the Twilio Client
        $this->twilioClient = new Client($twilioSid, $twilioAuthToken);
    }

    public function sendWhatsAppMessage(string $phoneNumber, string $message): void
    {
        $this->twilioClient->messages->create(
            'whatsapp:' . $phoneNumber, // The recipient's WhatsApp number
            [
                'from' => 'whatsapp:+1415523886', // Your Twilio WhatsApp Sandbox number
                'body' => $message,
            ]
        );
    }
}
