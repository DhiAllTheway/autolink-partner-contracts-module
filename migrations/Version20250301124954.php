<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250301124954 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE accord ADD CONSTRAINT FK_91361A0431159EE7 FOREIGN KEY (materiel_recyclable_id) REFERENCES materiel_recyclable (id)');
        $this->addSql('ALTER TABLE contract CHANGE status status VARCHAR(255) NOT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE accord DROP FOREIGN KEY FK_91361A0431159EE7');
        $this->addSql('ALTER TABLE contract CHANGE status status VARCHAR(255) DEFAULT \'pending\'');
    }
}
