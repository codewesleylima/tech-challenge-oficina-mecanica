/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a set of docs in the sidebar
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */

// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    tutorialSidebar: [
        'introducao',
        {
            label: 'DDD - Domain-Driven Design',
            items: [
                'ddd/introducao',
                'ddd/linguagem-ubiqua',
                'ddd/event-storming',
                'ddd/bounded-contexts',
                'ddd/agregados',
                'ddd/value-objects',
            ],
        },
        {
            label: 'Arquitetura de Software',
            items: [
                'arquitetura/adr',
                'arquitetura/decisoes-arquiteturais',
                'arquitetura/hexagonal',
                'arquitetura/diagramas',
            ],
        },
        {
            label: 'API REST',
            items: [
                'api/swagger',
                'api/endpoints',
                'api/autenticacao',
                'api/exemplos',
            ],
        },
        {
            label: 'Segurança',
            items: [
                'seguranca/introducao',
                'seguranca/vulnerabilidades',
                'seguranca/relatorio-analise',
                'seguranca/boas-praticas',
            ],
        },
        {
            label: 'Execução',
            items: [
                'execucao/docker-compose',
                'execucao/variveis-ambiente',
                'execucao/troubleshooting',
            ],
        },
    ],
};

export default sidebars;