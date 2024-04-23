# Use uma imagem base com Java 17
FROM openjdk:17-alpine

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR da sua aplicação para o contêiner
COPY build/libs/micronautguide-0.1-all.jar /app/micronautguide-0.1-all.jar

# Comando para executar a aplicação quando o contêiner iniciar
CMD ["java", "-jar", "micronautguide-0.1-all.jar"]
