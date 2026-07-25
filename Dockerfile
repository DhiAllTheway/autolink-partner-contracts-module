FROM php:8.2-fpm
# Install system dependencies
RUN apt-get update && apt-get install -y \
    git \
    unzip \
    libicu-dev \
    libzip-dev \
    libpng-dev \
    libjpeg-dev \
    libfreetype6-dev \
    default-mysql-client \
    default-libmysqlclient-dev \
    zip \
    && rm -rf /var/lib/apt/lists/*
# Install PHP extensions
RUN docker-php-ext-configure gd --with-jpeg --with-freetype \
    && docker-php-ext-install \
    intl \
    pdo \
    pdo_mysql \
    zip \
    opcache \
    gd
# Install Composer
COPY --from=composer:2 /usr/bin/composer /usr/bin/composer
WORKDIR /var/www/html
COPY . .
RUN composer install --no-dev --optimize-autoloader --no-interaction --no-scripts
RUN chown -R www-data:www-data /var/www/html
EXPOSE 9000
CMD ["php-fpm"]
