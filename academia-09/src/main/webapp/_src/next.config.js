/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
}

module.exports = {
  nextConfig,
  assetPrefix: './',
  images: {
    domains: [
      "avatars.githubusercontent.com",
    ],
  },
}
