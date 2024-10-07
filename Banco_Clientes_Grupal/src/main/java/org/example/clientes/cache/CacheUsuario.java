package org.example.clientes.cache;

import org.example.clientes.model.Usuario;
import org.example.common.Cache;

public interface CacheUsuario extends Cache<Long, Usuario> {
    int USUARIO_CACHE_SIZE = 5;
}
