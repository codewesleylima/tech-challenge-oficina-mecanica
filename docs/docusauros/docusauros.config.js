// @ts-check
// `@type` JSDoc annotations allow editor autocompletion and type checking
// (when paired with `@ts-check`).
// There are various equivalent ways to declare your Docusaurus config.
// See: https://docusaurus.io/docs/api/docusaurus-config

import {themes as prismThemes} from 'prism-react-renderer';

/** @type {import('@docusaurus/types').Config} */
const config = {
    title: 'Sistema de Gerenciamento de Oficina Mecânica',
    tagline: 'MVP - Documentação Técnica',
    favicon: 'img/favicon.ico',

    // Set the production url of your site here
    url: 'https://seu-dominio.com',
    // Set the /<baseUrl>/ pathname under which your site is served
    // For GitHub pages deployment, it is often '/<projectName>/'
    baseUrl: '/',

    // GitHub pages deployment config.
    // If you aren't using GitHub pages, you don't need these.
    organizationName: 'seu-usuario', // Usually your GitHub org/username.
    projectName: 'tech-challenge', // Usually your repo name.

    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',

    // Even if you don't use internationalization, you can use this field to set
    // useful metadata like html lang. For example, if your site is in Chinese, you
    // may want to replace "en" with "zh-Hans".
    i18n: {
        defaultLocale: 'pt-BR',
        locales: ['pt-BR'],
    },

    presets: [
        [
            'classic',
            /** @type {import('@docusaurus/preset-classic').Options} */
            ({
                docs: {
                    sidebarPath: './sidebars.js',
                    editUrl:
                        'https://github.com/seu-usuario/tech-challenge/tree/main/docs/',
                },
                blog: false,
                theme: {
                    customCss: './src/css/custom.css',
                },
            }),
        ],
    ],

    themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
        ({
            image: 'img/docusaurus-social-card.jpg',
            navbar: {
                title: 'Tech Challenge - Oficina Mecânica',
                logo: {
                    alt: 'Logo',
                    src: 'img/logo.svg',
                },
                items: [
                    {
                        type: 'docSidebar',
                        sidebarId: 'tutorialSidebar',
                        position: 'left',
                        label: 'Documentação',
                    },
                    {
                        href: 'https://github.com/seu-usuario/tech-challenge',
                        label: 'GitHub',
                        position: 'right',
                    },
                ],
            },
            footer: {
                style: 'dark',
                links: [
                    {
                        title: 'Documentação',
                        items: [
                            {
                                label: 'DDD',
                                to: '/docs/ddd/introducao',
                            },
                            {
                                label: 'Arquitetura',
                                to: '/docs/arquitetura/adr',
                            },
                            {
                                label: 'API',
                                to: '/docs/api/swagger',
                            },
                        ],
                    },
                    {
                        title: 'Projeto',
                        items: [
                            {
                                label: 'GitHub',
                                href: 'https://github.com/seu-usuario/tech-challenge',
                            },
                            {
                                label: 'Issues',
                                href: 'https://github.com/seu-usuario/tech-challenge/issues',
                            },
                        ],
                    },
                ],
                copyright: `Copyright © ${new Date().getFullYear()} Tech Challenge. Built with Docusaurus.`,
            },
            prism: {
                theme: prismThemes.github,
                darkTheme: prismThemes.dracula,
                additionalLanguages: ['java', 'bash', 'yaml', 'sql', 'json'],
            },
            colorMode: {
                defaultMode: 'dark',
                disableSwitch: false,
                respectPrefersColorScheme: true,
            },
        }),
};

export default config;