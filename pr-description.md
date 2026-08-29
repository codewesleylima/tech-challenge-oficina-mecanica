**📄 Descrição**
Adiciona a análise de vulnerabilidades das dependências com o OWASP Dependency-Check e versiona o relatório gerado pelo plugin. Inclui o plugin configurado no build, arquivo de supressões para falsos-positivos, o relatório HTML em `docs/security/` e uma seção no README explicando como rodar e onde consultar o resultado.

**🎯 Objetivo**
Atender o entregável de análise de vulnerabilidades (scan) do Tech Challenge, cobrindo a categoria **A06 – Vulnerable & Outdated Components** do OWASP Top 10, com evidência reproduzível e versionada.

**📏 Critérios de aceite**
- OWASP Dependency-Check configurado no `build.gradle` (`./gradlew dependencyCheckAnalyze`)
- Relatório do plugin versionado em `docs/security/dependency-check-report.html`
- README com seção de análise de vulnerabilidades e instruções de execução
- Arquivo `config/dependency-check-suppressions.xml` para registrar falsos-positivos com justificativa

**🔗 Referência Miro**
[Board de Event Storming / Software Design](https://miro.com/app/board/uXjVHIl34Zc=/?share_link_id=851279149523)
