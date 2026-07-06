<?php

namespace App\Service;

use chillerlan\QRCode\QRCode;
use chillerlan\QRCode\QROptions;

class QrCodeGenerator
{
    public function generateQrCode(string $data): string
    {
        $options = new QROptions([
            'outputType' => QRCode::OUTPUT_IMAGE_PNG,
            'eccLevel'   => QRCode::ECC_H,  // High error correction
            'scale'      => 5,  // Adjust the size
        ]);

        $qrCode = (new QRCode($options))->render($data);

        return base64_encode($qrCode);
    }
}
