package classe;

/**
 * Representa uma pessoa na empresa com informações básicas.
 */
public class Pessoa {
    // Identificador único da pessoa (gerado automaticamente pelo banco)
    private int id;
    // Nome completo da pessoa
    private String nome;
    // Endereço de e-mail para contato
    private String email;

    /**
     * Construtor padrão para a classe Pessoa.
     */
    public Pessoa() {
        // Construtor vazio para flexibilidade
    }

    /**
     * Construtor com parâmetros para inicializar uma Pessoa.
     *
     * @param id    Identificador único da pessoa (gerado automaticamente)
     * @param nome  Nome completo da pessoa
     * @param email Endereço de e-mail válido
     */
    public Pessoa(int id, String nome, String email) {
        this.id = id;
        // Usa setters para validação
        setNome(nome);
        setEmail(email);
    }

    /**
     * Obtém o ID da pessoa.
     *
     * @return o ID da pessoa
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID da pessoa.
     *
     * @param id o ID da pessoa
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome da pessoa.
     *
     * @return o nome da pessoa
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da pessoa.
     *
     * @param nome o nome da pessoa
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     */
    public void setNome(String nome) {
        // Valida se o nome é válido (Regra de Negócio 4)
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    /**
     * Obtém o e-mail da pessoa.
     *
     * @return o e-mail da pessoa
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail da pessoa.
     *
     * @param email o e-mail da pessoa
     * @throws IllegalArgumentException se o e-mail for nulo, vazio ou inválido
     */
    public void setEmail(String email) {
        // Valida se o e-mail é válido (Regra de Negócio 4)
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email;
    }

    /**
     * Retorna uma representação em string da pessoa.
     *
     * @return string com os detalhes da pessoa
     */
    @Override
    public String toString() {
        return "Pessoa [id=" + id + ", nome=" + nome + ", email=" + email + "]";
    }
}